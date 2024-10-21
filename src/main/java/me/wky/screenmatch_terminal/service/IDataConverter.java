package me.wky.screenmatch_terminal.service;

public interface IDataConverter {
    public <T> T convert(String json, Class<T> tClass);
}
