package com.tasks.viemed.domain.mapper

interface DomainEntityMapper<E, D> {

    fun toEntity(d: D): E

    fun toDomain(e: E): D

}