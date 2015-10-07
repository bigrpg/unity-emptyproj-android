package com.zulong.sdk.core.open;

public class UserInfo
{
  private String roleId;
  private String roleName;
  private String lv;
  private int zoneId;
  private String zoneName;

  public String getRoleId()
  {
    return this.roleId;
  }

  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }

  public String getRoleName()
  {
    return this.roleName;
  }

  public void setRoleName(String roleName)
  {
    this.roleName = roleName;
  }

  public String getLv()
  {
    return this.lv;
  }

  public void setLv(String lv)
  {
    this.lv = lv;
  }

  public int getZoneId()
  {
    return this.zoneId;
  }

  public void setZoneId(int zoneId)
  {
    this.zoneId = zoneId;
  }

  public String getZoneName()
  {
    return this.zoneName;
  }

  public void setZoneName(String zoneName)
  {
    this.zoneName = zoneName;
  }

  public String toString()
  {
    return "UserInfo [roleId=" + this.roleId + ", roleName=" + this.roleName + ", lv=" + this.lv + ", zoneId=" + this.zoneId + ", zoneName=" + this.zoneName + "]";
  }
}