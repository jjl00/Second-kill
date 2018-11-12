--库存总量
local key1 = KEYS[1]

--当前销售量
local key2 = KEYS[2]

local sum = tonumber(redis.call('get', key1) or "0")
local sold = tonumber(redis.call('get', key2) or "0")
--如果销售量大于库存总量或者未将key1,key2放入redis，则返回0，表示失败
if(sold>=sum or sum=="nil" or sold=="nil")
then
    return 0;
else
--销售量加一。另外incrby可以在键不存在时自动创建，初值为0
    redis.call("INCRBY", key2, 1)
    return 1;
end

