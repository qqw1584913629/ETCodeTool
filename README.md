ET框架8.1代码生成工具

## 功能特点

- 支持生成 Entity、Component、System 和 Handler 代码
- 支持自动添加常用接口实现
- 支持 Server、Client 和 Share 命名空间
- 支持消息处理器代码生成
- 提供友好的图形界面配置

## 注意事项

- Entity 类不会自动添加后缀
- Component 类会自动添加 Component 后缀
- System 类会自动添加 System 后缀
- Handler 类会自动添加 Handler 后缀
- Handler 类命名需遵循 X2Y_ZZZ 格式，否则需要手动填写泛型

## 使用方法

1. 右键点击项目文件夹
2. 选择 New -> ET Code
3. 在弹出的对话框中配置相关选项
4. 点击确定生成代码

如有问题，请提Issue