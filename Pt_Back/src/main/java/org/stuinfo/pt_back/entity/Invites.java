package org.stuinfo.pt_back.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author lt
 * @since 2025-04-26
 */
@ApiModel(value = "Invites对象", description = "")
public class Invites implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("code")
    private String code;

    private Integer generatedBy;

    private Integer usedBy;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(Integer generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Integer getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(Integer usedBy) {
        this.usedBy = usedBy;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Invites{" +
            "code = " + code +
            ", generatedBy = " + generatedBy +
            ", usedBy = " + usedBy +
            ", expiresAt = " + expiresAt +
            ", createdAt = " + createdAt +
        "}";
    }
}
