import Mock from 'mockjs'

Mock.mock('/api/users', 'get', {
  'users|10': [{
    'id|+1': 1,
    'name': '@name',
    'email': '@email'
  }]
}) 