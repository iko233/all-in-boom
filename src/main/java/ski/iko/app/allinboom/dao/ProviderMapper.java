package ski.iko.app.allinboom.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import ski.iko.app.allinboom.dao.po.ProviderDo;

@Mapper
@Repository
public interface ProviderMapper extends BaseMapper<ProviderDo> {
}
