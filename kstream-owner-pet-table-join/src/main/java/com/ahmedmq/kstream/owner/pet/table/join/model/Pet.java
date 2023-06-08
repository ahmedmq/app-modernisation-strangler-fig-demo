package com.ahmedmq.kstream.owner.pet.table.join.model;

public record Pet(Integer id, String name, String birth_date, int type_id, Integer owner_id) {
}