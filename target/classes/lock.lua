-- 意思就是发送过去 随机值 和key比较 如果相同的话就是自己的value  如果不同就不是自己的value
if redis.call("get",KEYS[1])==ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
