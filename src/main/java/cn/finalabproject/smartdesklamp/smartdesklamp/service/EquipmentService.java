package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Equipment;

public interface EquipmentService {
    public int InserEquipment(Equipment equipment);

    public boolean deleteEquipment(Integer id);

    public boolean updateEquipment(Equipment equipment);

    public Equipment queryEquipmentById(Integer id);

    public void switchMode(Integer type,Integer eid);

    public void adjustBrightness(Integer brightness,Integer eid);

    public boolean alterCurrentMusicId(Integer eid,Integer musicId);

    public Integer getCurrentMusicId(Integer eid);
}
