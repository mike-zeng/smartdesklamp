package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Equipment;

public interface EquipmentService {
    public int InserEquipment(Equipment equipment);

    public boolean deleteEquipment(Integer id);

    public boolean updateEquipment(Equipment equipment);

    public Equipment queryEquipmentById(Integer id);
}
