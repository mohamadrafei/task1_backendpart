package com.example.demo.web.controller;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.application.port.in.AddMaterialCommand;
import com.example.demo.application.port.in.AddMaterialUseCase;
import com.example.demo.application.port.in.AssignJobUseCase;
import com.example.demo.application.port.in.CreateJobCommand;
import com.example.demo.application.port.in.CreateJobUseCase;
import com.example.demo.application.port.in.GetJobsForCompanyUseCase;
import com.example.demo.application.port.in.GetJobsForTechnicianUseCase;
import com.example.demo.application.port.in.GetMaterialsForJobUseCase;
import com.example.demo.application.port.in.JobTimeTrackingUseCase;
import com.example.demo.application.port.in.UpdateJobStatusCommand;
import com.example.demo.application.port.in.UpdateJobStatusUseCase;
import com.example.demo.application.port.service.PhotoUploadService;
import com.example.demo.domain.Entities.Job;
import com.example.demo.domain.Entities.MaterialUsage;
import com.example.demo.domain.Entities.User;
import com.example.demo.domain.Enums.JobStatus;
import com.example.demo.infrastructure.persistence.repository.UserRepository;
import com.example.demo.web.dto.AddMaterialRequest;
import com.example.demo.web.dto.CreateJobRequest;
import com.example.demo.web.dto.JobResponse;
import com.example.demo.web.dto.MaterialUsageResponse;
import com.example.demo.web.dto.UpdateJobStatusRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

        private final CreateJobUseCase createJobUseCase;
        private final AssignJobUseCase assignJobUseCase;
        private final GetJobsForTechnicianUseCase getJobsForTechnicianUseCase;
        private final UpdateJobStatusUseCase updateJobStatusUseCase;
        private final PhotoUploadService photoUploadService;
        private final AddMaterialUseCase addMaterialUseCase;
        private final GetMaterialsForJobUseCase getMaterialsForJobUseCase;

        // -------------------- CREATE JOB (ADMIN / DISPATCHER) --------------------

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN','DISPATCHER')")
        @PostMapping
        public JobResponse createJob(
                        @AuthenticationPrincipal User currentUser,
                        @Valid @RequestBody CreateJobRequest request) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                var cmd = new CreateJobCommand(
                                currentUser.getCompanyId(),
                                request.title(),
                                request.description(),
                                request.priority(),
                                request.address(),
                                request.latitude(),
                                request.longitude(),
                                request.scheduledStart(),
                                request.scheduledEnd(),
                                currentUser.getId());

                Job job = createJobUseCase.createJob(cmd);
                return toResponse(job);
        }

        // -------------------- PHOTO UPLOAD (TECHNICIAN) --------------------

        @PreAuthorize("hasRole('TECHNICIAN')")
        @PostMapping("/{jobId}/upload-photo")
        public String uploadJobPhoto(
                        @PathVariable Long jobId,
                        @AuthenticationPrincipal User currentUser,
                        @RequestParam("file") MultipartFile file) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                return photoUploadService.savePhoto(jobId, file);
        }

        // -------------------- MATERIALS (TECHNICIAN + VIEWERS) --------------------

        @PreAuthorize("hasRole('TECHNICIAN')")
        @PostMapping("/{jobId}/materials")
        public MaterialUsageResponse addMaterial(
                        @PathVariable Long jobId,
                        @AuthenticationPrincipal User currentUser,
                        @RequestBody AddMaterialRequest request) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                var cmd = new AddMaterialCommand(
                                jobId,
                                currentUser.getId(),
                                request.name(),
                                request.quantity(),
                                request.unit(),
                                request.unitPrice());

                MaterialUsage saved = addMaterialUseCase.addMaterial(cmd);

                return toMaterialResponse(saved);
        }

        @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN','COMPANY_ADMIN','DISPATCHER')")
        @GetMapping("/{jobId}/materials")
        public List<MaterialUsageResponse> getMaterials(
                        @PathVariable Long jobId,
                        @AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                var list = getMaterialsForJobUseCase.getMaterials(jobId);

                return list.stream()
                                .map(this::toMaterialResponse)
                                .toList();
        }

        private final GetJobsForCompanyUseCase getJobsForCompanyUseCase;

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN','DISPATCHER')")
        @GetMapping
        public List<JobResponse> getAllJobs(
                        @AuthenticationPrincipal User currentUser) {
                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                var jobs = getJobsForCompanyUseCase.getJobsForCompany(
                                currentUser.getCompanyId());

                return jobs.stream()
                                .map(this::toResponse)
                                .toList();
        }

        // -------------------- ASSIGN JOB (ADMIN / DISPATCHER) --------------------
        private final UserRepository UserRepository;

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN','DISPATCHER')")
        @PutMapping("/{jobId}/assign")
        public void assignJob(
                        @PathVariable Long jobId,
                        @RequestParam String technicianEmail,
                        @AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                User technician = UserRepository
                                .findByCompanyIdAndEmailIgnoreCase(
                                                currentUser.getCompanyId(),
                                                technicianEmail)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Technician not found with email: " + technicianEmail));

                assignJobUseCase.assignJob(jobId, technician.getId(), currentUser.getId());
        }

        private final JobTimeTrackingUseCase jobTimeTrackingUseCase;

        @PreAuthorize("hasRole('TECHNICIAN')")
        @PutMapping("/{jobId}/start-work")
        public void startWork(
                        @PathVariable Long jobId,
                        @AuthenticationPrincipal User currentUser) {

                System.out.println("========== START WORK DEBUG ==========");
                System.out.println("🔍 Job ID: " + jobId);

                if (currentUser == null) {
                        System.out.println("❌ Current user is NULL");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                System.out.println("✅ Current user ID: " + currentUser.getId());
                System.out.println("✅ Current user email: " + currentUser.getEmail());
                System.out.println("✅ Current user roles: " + currentUser.getRoles());

                try {
                        jobTimeTrackingUseCase.startWork(jobId, currentUser.getId());
                        System.out.println("✅ Start work completed successfully");
                } catch (Exception e) {
                        System.out.println("❌ Error in startWork: " + e.getMessage());
                        throw e;
                }
                System.out.println("======================================");
        }

        @PreAuthorize("hasRole('TECHNICIAN')")
        @PutMapping("/{jobId}/complete-work")
        public void completeWork(
                        @PathVariable Long jobId,
                        @AuthenticationPrincipal User currentUser) {

                System.out.println("========== COMPLETE WORK DEBUG ==========");
                System.out.println("🔍 Job ID: " + jobId);

                if (currentUser == null) {
                        System.out.println("❌ Current user is NULL");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                System.out.println("✅ Current user ID: " + currentUser.getId());
                System.out.println("✅ Current user email: " + currentUser.getEmail());

                try {
                        jobTimeTrackingUseCase.completeWork(jobId, currentUser.getId());
                        System.out.println("✅ Complete work succeeded");
                } catch (Exception e) {
                        System.out.println("❌ Error in completeWork: " + e.getMessage());
                        throw e;
                }
                System.out.println("=========================================");
        }
        // -------------------- UPDATE STATUS (TECHNICIAN) --------------------

        @PreAuthorize("hasRole('TECHNICIAN')")
        @PutMapping("/{jobId}/status")
        public void updateJobStatus(
                        @PathVariable Long jobId,
                        @RequestBody UpdateJobStatusRequest request,
                        @AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                JobStatus newStatus;
                try {
                        newStatus = JobStatus.valueOf(request.status().toUpperCase());
                } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
                }

                var cmd = new UpdateJobStatusCommand(
                                jobId,
                                currentUser.getId(), // technician id from token
                                newStatus);

                updateJobStatusUseCase.updateStatus(cmd);
        }

        // -------------------- MY TODAY JOBS (TECHNICIAN) --------------------

        @PreAuthorize("hasRole('TECHNICIAN')")
        @GetMapping("/my-jobs")
        public List<JobResponse> myJobs(@AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                // ✅ Only jobs where this technician is assigned
                var jobs = getJobsForTechnicianUseCase.getAllJobs(currentUser.getId());

                return jobs.stream()
                                .map(this::toResponse)
                                .toList();
        }

        @PreAuthorize("hasAnyRole('ADMIN','COMPANY_ADMIN','DISPATCHER')")
        @GetMapping("/all")
        public List<JobResponse> getAllJobsForAdmin(
                        @AuthenticationPrincipal User currentUser) {

                if (currentUser == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
                }

                var jobs = getJobsForCompanyUseCase.getJobsForCompany(currentUser.getCompanyId());

                return jobs.stream()
                                .map(this::toResponse)
                                .toList();
        }

        // -------------------- MAPPERS --------------------

        private JobResponse toResponse(Job job) {
                return new JobResponse(
                                job.getId(),
                                job.getTitle(),
                                job.getDescription(),
                                job.getStatus().name(),
                                job.getPriority(),
                                job.getAddress(),
                                job.getAssignedTechnicianId());
        }

        private MaterialUsageResponse toMaterialResponse(MaterialUsage m) {
                return new MaterialUsageResponse(
                                m.getId(),
                                m.getJobId(),
                                m.getTechnicianId(),
                                m.getName(),
                                m.getQuantity(),
                                m.getUnit(),
                                m.getUnitPrice(),
                                m.getCreatedAt());
        }
}
