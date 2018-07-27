/*
 * Copyright 2018 https://www.valiktor.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.valiktor.springframework.web.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import org.valiktor.springframework.config.ValiktorConfiguration
import org.valiktor.springframework.web.payload.UnprocessableEntity
import org.valiktor.springframework.web.payload.ValidationConstraint
import org.valiktor.springframework.web.payload.ValidationError
import org.valiktor.springframework.web.payload.ValidationParam

/**
 * Represents the REST controller that handle [ConstraintViolationException] and returns an appropriate HTTP response.
 *
 * @param config specifies the [ValiktorConfiguration]
 *
 * @author Rodolpho S. Couto
 * @see ConstraintViolationException
 * @see ExceptionHandler
 * @since 0.1.0
 */
@RestControllerAdvice
class ValiktorExceptionHandler(private val config: ValiktorConfiguration) {

    /**
     * Handle [ConstraintViolationException] and returns 422 (Unprocessable Entity) status code
     * with the constraint violations.
     *
     * @param ex specifies the [ConstraintViolationException]
     * @param acceptLanguage specifies the Accept-Language HTTP header of the request
     * @return the ResponseEntity with 422 status code and the constraint violations
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException,
                                           acceptLanguage: AcceptHeaderLocaleResolver): ResponseEntity<UnprocessableEntity> =
            ResponseEntity
                    .unprocessableEntity()
                    .body(UnprocessableEntity(errors = ex.constraintViolations
                            .asSequence()
                            .mapToMessage(baseName = config.bundleBaseName, locale = acceptLanguage.defaultLocale)
                            .map {
                                ValidationError(
                                        property = it.property,
                                        value = it.value,
                                        message = it.message,
                                        constraint = ValidationConstraint(
                                                name = it.constraint.name,
                                                params = it.constraint.messageParams
                                                        .asSequence()
                                                        .map {
                                                            ValidationParam(
                                                                    name = it.key,
                                                                    value = it.value
                                                            )
                                                        }
                                                        .toSet()
                                        )
                                )
                            }
                            .toSet()))
}