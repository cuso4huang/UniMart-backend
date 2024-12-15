const express = require('express')
const app = express()

// 中间件配置
app.use(express.json())

// API 路由
app.get('/api/data', (req, res) => {
  res.json({ message: 'Hello from Express!' })
})

app.listen(3000, () => {
  console.log('Server running on port 3000')
}) 