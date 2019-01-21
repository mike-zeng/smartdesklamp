package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Equipment;

public interface EquipmentService {
    public int InserEquipment(Equipment equipment);

    public boolean deleteEquipment(Integer id);

    public boolean updateEquipment(Equipment equipment);

    public Equipment queryEquipmentById(Integer id);

    public boolean updateUserId(Integer uid, Integer equipmentId);

    //获取使用者的id
    public Integer getUserId(Integer equipmentId);

    public void switchMode(Integer type,Integer eid);

    public void adjustBrightness(Integer brightness,Integer eid);

    public boolean alterCurrentMusicId(Integer eid,Integer musicId);

    public Integer getCurrentMusicId(Integer eid);
}
