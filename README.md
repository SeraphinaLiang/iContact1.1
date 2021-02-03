# iContact1.1 本科小课设

1. 最后打包的时候，把图片都放进了src里面。

2. 不在fxml里面的图片（代码加载的图片）路径变为相对路径。ex. private Image img = new Image("parade.png");再把图片复制到src下面。
   修改的代码：1.GroupCell类，PersonCell类
              2.MyPageControl(photo处),ContactPageControl(photo处)
   可参考iContact1.2（代码是错的）的结构。
              
3. 未解决的问题：生成jar之后，上传图片路径出错，无法导入导出数据库/导入导出文件。
