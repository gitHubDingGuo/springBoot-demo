package top.javahouse.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.javahouse.mybatisplus.entity.User;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
