package net.shopin.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author littledream1502@gmail.com
 * @date 2018/1/4
 * @desc controller 统一返回结果
 * 现阶段对于此模块的问题在于不同项目所需的common是不出现交集的,所以要根据实际情况实际分析;
 */
@Data
public class ResultBean<T> implements Serializable {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int NO_PERMISSION = 2;

    private static final long serialVersionUID = -2009666727270407611L;

    public String msg = "success";
    public int code = SUCCESS;

    public T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }
}
