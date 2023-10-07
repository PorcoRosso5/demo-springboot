# Design for Dynamic Excel Read&Write

## Graph Flow

### read excel
1. application run
2. prepare class by javassist, using template and template_field
3. create ExcelService and ReadListener of each template, register in spring

### write excel


```mermaid
flowchart
    a["s"]
    b["s"]
    c["s"]
    d["s"]
```

## sync config 
### not restart
https://blog.csdn.net/m0_46267097/article/details/130139102
### restart
https://blog.csdn.net/weixin_42304484/article/details/129638023
https://www.ramostear.com/2020/05/howtorestartspringbootatruntime.html
