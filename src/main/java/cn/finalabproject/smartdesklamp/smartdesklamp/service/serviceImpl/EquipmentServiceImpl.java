package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.WebSocket.MessageSocketServer;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EquipmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Equipment;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public int InserEquipment(Equipment equipment) {
        return equipmentMapper.InsertEquipment(equipment);
    }

    @Override
    public boolean deleteEquipment(Integer id) {
        return equipmentMapper.deleteEquipment(id);
    }

    @Override
    public boolean updateEquipment(Equipment equipment) {
        return equipmentMapper.updateEquipment(equipment);
    }

    @Override
    public Equipment queryEquipmentById(Integer id) {
        return equipmentMapper.queryEquipmentById(id);
    }

    @Override
    public void switchMode(Integer type,Integer eid) {
        MessageSocketServer.sentToHardWare(eid,"Switch-" + type);
    }

    @Override
    public void adjustBrightness(Integer brightness,Integer eid) {
        MessageSocketServer.sentToHardWare(eid,"adjust-" + brightness);
    }

    @Override
    public boolean alterCurrentMusicId(Integer eid, Integer musicId) {
        return equipmentMapper.alterCurrentMusicId(eid,musicId);
    }

    @Override
    public Integer getCurrentMusicId(Integer eid) {
        return equipmentMapper.getCurrentMusicId(eid);
    }
}
