package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Equipment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EquipmentMapper {
    @Insert({"insert into equipment(kind,name) values(#{kind},#{name})"})
    @Options(useGeneratedKeys = true,keyColumn = "equipment_id",keyProperty = "equipmentId")
    public int InserEquipment(Equipment equipment);

    @Delete("delete from equipment where equipment_id=#{equipmentId}")
    public boolean deleteEquipment(@Param("equipmentId")Integer equipmentId);

    @Update("update equipment set kind=#{kind},name=#{name} where equipment_id=#{equipmentId}")
    public boolean updateEquipment(Equipment equipment);

    @Select("select * from equipment where equipment_id=#{equipmentId}")
    @Results({
            @Result(property = "equipmentId",column = "equipment_id")
    })
    public Equipment queryEquipmentById(@Param("equipmentId") Integer equipmentId);
}
