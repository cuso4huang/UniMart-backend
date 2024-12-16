// 检查静态文件服务的配置
app.use('/uploads', express.static('uploads'))  // 确保这样配置

// 或者应该是
app.use('/public/uploads', express.static('uploads')) 