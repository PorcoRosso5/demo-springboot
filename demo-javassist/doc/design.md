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