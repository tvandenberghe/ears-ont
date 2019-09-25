package be.naturalsciences.bmdc.ears.ontology.rest;

public abstract interface IResponseMessage
{
  public abstract String getCode();
  
  public abstract String getSummary();
  
  public abstract int getStatus();
  
  public abstract void setStatus(int paramInt);
  
  public abstract boolean isBad();
}