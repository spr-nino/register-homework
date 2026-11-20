package com.dino.registermodule.dao;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.domain.entity.ThirdPartAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 第三方登录账号（{@link ThirdPartAccount}）的数据访问接口，提供第三方账号的绑定查询与维护操作。
 */
public interface ThirdPartAccountDao extends BaseDao<ThirdPartAccount> {
    /**
     * 用户第三方绑定，新增一条第三方账号绑定记录。
     *
     * @param thirdPartAccount 待绑定的第三方账号信息（含 loginId、openId、平台类型）
     */
    void bindThirdPartAccount(ThirdPartAccount thirdPartAccount);
    /**
     * 通过id查找第三方用户数量，统计指定 openId 的绑定记录数。
     *
     * @param openId 第三方平台返回的用户唯一标识
     * @return 该 openId 对应的绑定记录数
     */
    int countByOpenId(@Param("openId") String openId);
    /**
     * 通过loginId查找第三方用户列表，返回该登录账号已绑定的全部第三方账号。
     *
     * @param loginId 登录账号ID
     * @return 该登录账号绑定的第三方账号列表
     */
    List<ThirdPartAccount> selectThirdPartAccountByLoginId(@Param("loginId") String loginId);
    /**
     * 更新第三方表，将原登录账号名下的绑定记录转移到新的登录账号名下。
     *
     * @param delLoginId 原登录账号ID
     * @param newLoginId 新的登录账号ID
     */
   void updateLoginId(@Param("delLoginId") String delLoginId, @Param("newLoginId") String newLoginId) ;
    /**
     * 用户取消绑定第三方，按 openId 删除对应的绑定记录。
     *
     * @param openId 第三方平台返回的用户唯一标识
     */
    void cancelBindThirdPartAccount(@Param("openId") String openId);

    /**
     * 批量插入第三方账号绑定记录。
     *
     * @param thirdPartAccountList 待插入的第三方账号列表
     */
    void batchInsert(List<ThirdPartAccount> thirdPartAccountList);
}
