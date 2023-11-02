package common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commf.dao.MyBatisMappingDAO;

/**
 * Program Name     : BaseDAO
 * Description      :
 * Programmer Name  : ntarget
 * Creation Date    : 2021-02-08
 * Used Table       :
 *
 *   수정일      수정자                     수정내용
 *  -------    --------    ---------------------------
 *
 */
public class BaseDAO extends MyBatisMappingDAO {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseDAO() {

    }
}
