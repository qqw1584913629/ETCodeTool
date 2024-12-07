package com.example.demo.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import javax.swing.*
import java.awt.GridLayout
import java.awt.BorderLayout
import java.awt.FlowLayout

class GenerateTextAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR)

        // 创建对话框
        val dialog = object : DialogWrapper(project) {
            // 第一组单选框
            val serverRadio = JRadioButton("Server")
            val clientRadio = JRadioButton("Client") 
            val shareRadio = JRadioButton("Share")
            private val buttonGroup1 = ButtonGroup()
            
            // 第二组单选框
            val componentRadio = JRadioButton("Component")
            val systemRadio = JRadioButton("System")
            private val buttonGroup2 = ButtonGroup()
            
            // 第三组单选框
            val childOfRadio = JRadioButton("ChildOf")
            val componentOfRadio = JRadioButton("ComponentOf")
            private val buttonGroup3 = ButtonGroup()
            
            // 添加接口复选框
            val iAwakeCheckBox = JCheckBox("IAwake")
            val iUpdateCheckBox = JCheckBox("IUpdate")
            val iLateUpdateCheckBox = JCheckBox("ILateUpdateSystem")
            val iDestroyCheckBox = JCheckBox("IDestroy")
            val iGetComponentSysCheckBox = JCheckBox("IGetComponentSys")
            val iSerializeCheckBox = JCheckBox("ISerialize")
            val iDeserializeCheckBox = JCheckBox("IDeserialize")
            
            // 接口选择面板
            private val interfacePanel = JPanel(FlowLayout(FlowLayout.LEFT))
            
            val nameTextField = JTextField(20)

            init {
                title = "选择生成类型"
                init()

                // 添加第一组单选框
                buttonGroup1.add(serverRadio)
                buttonGroup1.add(clientRadio)
                buttonGroup1.add(shareRadio)
                
                // 添加第二组单选框
                buttonGroup2.add(componentRadio)
                buttonGroup2.add(systemRadio)
                
                // 添加第三组单选框
                buttonGroup3.add(childOfRadio)
                buttonGroup3.add(componentOfRadio)
                
                // 默认选中Server、Component和ChildOf
                serverRadio.isSelected = true
                componentRadio.isSelected = true
                childOfRadio.isSelected = true
                
                // 默认选中IAwake和IDestroy
                iAwakeCheckBox.isSelected = true
                iDestroyCheckBox.isSelected = true
                
                // 添加单选框切换监听器
                componentRadio.addActionListener { interfacePanel.isVisible = true }
                systemRadio.addActionListener { interfacePanel.isVisible = false }
            }

            override fun createCenterPanel(): JComponent {
                val mainPanel = JPanel(GridLayout(5, 1))
                
                // 第一组单选框面板
                val group1Panel = JPanel(FlowLayout(FlowLayout.LEFT))
                group1Panel.add(JLabel("命名空间:            "))
                group1Panel.add(serverRadio)
                group1Panel.add(clientRadio)
                group1Panel.add(shareRadio)
                mainPanel.add(group1Panel)
                
                // 第二组单选框面板
                val group2Panel = JPanel(FlowLayout(FlowLayout.LEFT))
                group2Panel.add(JLabel("组件类型:            "))
                group2Panel.add(componentRadio)
                group2Panel.add(systemRadio)
                mainPanel.add(group2Panel)
                
                // 第三组单选框面板
                val group3Panel = JPanel(FlowLayout(FlowLayout.LEFT))
                group3Panel.add(JLabel("标签类型:            "))
                group3Panel.add(childOfRadio)
                group3Panel.add(componentOfRadio)
                group3Panel.add(JLabel("    标签值:"))
                group3Panel.add(JTextField(10))
                mainPanel.add(group3Panel)
                
                // 配置接口复选框面板
                interfacePanel.add(JLabel("接口选择:            "))
                interfacePanel.add(iAwakeCheckBox)
                interfacePanel.add(iUpdateCheckBox)
                interfacePanel.add(iLateUpdateCheckBox)
                interfacePanel.add(iDestroyCheckBox)
                interfacePanel.add(iGetComponentSysCheckBox)
                interfacePanel.add(iSerializeCheckBox)
                interfacePanel.add(iDeserializeCheckBox)
                mainPanel.add(interfacePanel)
                
                val inputPanel = JPanel(FlowLayout(FlowLayout.LEFT))
                inputPanel.add(JLabel("名称:                "))
                inputPanel.add(nameTextField)
                mainPanel.add(inputPanel)
                
                return mainPanel
            }
        }

        if (dialog.showAndGet() && editor != null) {
            val document = editor.document
            val caretModel = editor.caretModel
            val offset = caretModel.offset
            
            // 获取选中的选项
            val selectedText = when {
                dialog.serverRadio.isSelected -> "Server"
                dialog.clientRadio.isSelected -> "Client"
                dialog.componentRadio.isSelected -> "Component"
                dialog.systemRadio.isSelected -> "System"
                else -> "Share"
            }
            
            val name = dialog.nameTextField.text
            document.insertString(offset, "$selectedText: $name")
        }
    }
}