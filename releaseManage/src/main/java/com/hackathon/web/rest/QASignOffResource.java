package com.hackathon.web.rest;

import com.hackathon.domain.QASignOff;
import com.hackathon.repository.QASignOffRepository;
import com.hackathon.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hackathon.domain.QASignOff}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QASignOffResource {

    private final Logger log = LoggerFactory.getLogger(QASignOffResource.class);

    private static final String ENTITY_NAME = "releaseManageQaSignOff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QASignOffRepository qASignOffRepository;

    public QASignOffResource(QASignOffRepository qASignOffRepository) {
        this.qASignOffRepository = qASignOffRepository;
    }

    /**
     * {@code POST  /qa-sign-offs} : Create a new qASignOff.
     *
     * @param qASignOff the qASignOff to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qASignOff, or with status {@code 400 (Bad Request)} if the qASignOff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/qa-sign-offs")
    public ResponseEntity<QASignOff> createQASignOff(@RequestBody QASignOff qASignOff) throws URISyntaxException {
        log.debug("REST request to save QASignOff : {}", qASignOff);
        if (qASignOff.getId() != null) {
            throw new BadRequestAlertException("A new qASignOff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QASignOff result = qASignOffRepository.save(qASignOff);
        return ResponseEntity
            .created(new URI("/api/qa-sign-offs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /qa-sign-offs/:id} : Updates an existing qASignOff.
     *
     * @param id the id of the qASignOff to save.
     * @param qASignOff the qASignOff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qASignOff,
     * or with status {@code 400 (Bad Request)} if the qASignOff is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qASignOff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/qa-sign-offs/{id}")
    public ResponseEntity<QASignOff> updateQASignOff(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QASignOff qASignOff
    ) throws URISyntaxException {
        log.debug("REST request to update QASignOff : {}, {}", id, qASignOff);
        if (qASignOff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qASignOff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qASignOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QASignOff result = qASignOffRepository.save(qASignOff);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qASignOff.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /qa-sign-offs/:id} : Partial updates given fields of an existing qASignOff, field will ignore if it is null
     *
     * @param id the id of the qASignOff to save.
     * @param qASignOff the qASignOff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qASignOff,
     * or with status {@code 400 (Bad Request)} if the qASignOff is not valid,
     * or with status {@code 404 (Not Found)} if the qASignOff is not found,
     * or with status {@code 500 (Internal Server Error)} if the qASignOff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/qa-sign-offs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QASignOff> partialUpdateQASignOff(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QASignOff qASignOff
    ) throws URISyntaxException {
        log.debug("REST request to partial update QASignOff partially : {}, {}", id, qASignOff);
        if (qASignOff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qASignOff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qASignOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QASignOff> result = qASignOffRepository
            .findById(qASignOff.getId())
            .map(existingQASignOff -> {
                if (qASignOff.getSignOffId() != null) {
                    existingQASignOff.setSignOffId(qASignOff.getSignOffId());
                }
                if (qASignOff.getQaTested() != null) {
                    existingQASignOff.setQaTested(qASignOff.getQaTested());
                }
                if (qASignOff.getQaTestedBy() != null) {
                    existingQASignOff.setQaTestedBy(qASignOff.getQaTestedBy());
                }
                if (qASignOff.getSignOffAttached() != null) {
                    existingQASignOff.setSignOffAttached(qASignOff.getSignOffAttached());
                }
                if (qASignOff.getSignOffTo() != null) {
                    existingQASignOff.setSignOffTo(qASignOff.getSignOffTo());
                }
                if (qASignOff.getSignOffCc() != null) {
                    existingQASignOff.setSignOffCc(qASignOff.getSignOffCc());
                }
                if (qASignOff.getSignOffAttachments() != null) {
                    existingQASignOff.setSignOffAttachments(qASignOff.getSignOffAttachments());
                }

                return existingQASignOff;
            })
            .map(qASignOffRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qASignOff.getId().toString())
        );
    }

    /**
     * {@code GET  /qa-sign-offs} : get all the qASignOffs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qASignOffs in body.
     */
    @GetMapping("/qa-sign-offs")
    public List<QASignOff> getAllQASignOffs() {
        log.debug("REST request to get all QASignOffs");
        return qASignOffRepository.findAll();
    }

    /**
     * {@code GET  /qa-sign-offs/:id} : get the "id" qASignOff.
     *
     * @param id the id of the qASignOff to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qASignOff, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/qa-sign-offs/{id}")
    public ResponseEntity<QASignOff> getQASignOff(@PathVariable Long id) {
        log.debug("REST request to get QASignOff : {}", id);
        Optional<QASignOff> qASignOff = qASignOffRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(qASignOff);
    }

    /**
     * {@code DELETE  /qa-sign-offs/:id} : delete the "id" qASignOff.
     *
     * @param id the id of the qASignOff to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/qa-sign-offs/{id}")
    public ResponseEntity<Void> deleteQASignOff(@PathVariable Long id) {
        log.debug("REST request to delete QASignOff : {}", id);
        qASignOffRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
