package com.example.demo.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.PlainTextFileType
import javax.swing.*
import java.awt.GridLayout
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Dimension
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiDirectory

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
            val entityRadio = JRadioButton("Entity")
            val componentRadio = JRadioButton("Component")
            val systemRadio = JRadioButton("System")
            val handlerRadio = JRadioButton("Handler")
            private val buttonGroup2 = ButtonGroup()
            
            // 第三组单选框
            val childOfRadio = JRadioButton("ChildOf")
            val componentOfRadio = JRadioButton("ComponentOf")
            private val buttonGroup3 = ButtonGroup()

            // 第四组单选框
            val messageHandlerRadio = JRadioButton("MessageHandler")
            val messageSessionHandlerRadio = JRadioButton("MessageSessionHandler")
            val messageLocationHandlerRadio = JRadioButton("MessageLocationHandler")
            private val buttonGroup4 = ButtonGroup()
            private val group4Panel = JPanel(FlowLayout(FlowLayout.LEFT))
            
            // 添加接口复选框
            val iAwakeCheckBox = JCheckBox("IAwake")
            val iUpdateCheckBox = JCheckBox("IUpdate")
            val iLateUpdateCheckBox = JCheckBox("ILateUpdateSystem")
            val iDestroyCheckBox = JCheckBox("IDestroy")
            val iGetComponentSysCheckBox = JCheckBox("IGetComponentSys")
            val iSerializeCheckBox = JCheckBox("ISerialize")
            val iDeserializeCheckBox = JCheckBox("IDeserialize")
            
            // 接口选择面板
            private val interfacePanel = JPanel(GridLayout(3, 3, 5, 5))
            
            // 第三组单选框面板
            private val group3Panel = JPanel(FlowLayout(FlowLayout.LEFT))
            
            val nameTextField = JTextField(20)
            val tagValueField = JTextField(10)
            
            // 主面板
            private lateinit var mainPanel: JPanel

            init {
                title = "框架代码生成"
                init()

                // 添加第一组单选框
                buttonGroup1.add(serverRadio)
                buttonGroup1.add(clientRadio)
                buttonGroup1.add(shareRadio)
                
                // 添加第二组单选框
                buttonGroup2.add(entityRadio)
                buttonGroup2.add(componentRadio)
                buttonGroup2.add(systemRadio)
                buttonGroup2.add(handlerRadio)
                
                // 添加第三组单选框
                buttonGroup3.add(childOfRadio)
                buttonGroup3.add(componentOfRadio)

                // 添加第四组单选框
                buttonGroup4.add(messageHandlerRadio)
                buttonGroup4.add(messageSessionHandlerRadio)
                buttonGroup4.add(messageLocationHandlerRadio)
                
                // 默认选中Server和Entity
                serverRadio.isSelected = true
                entityRadio.isSelected = true
                childOfRadio.isSelected = true
                
                // 默认选中IAwake和IDestroy
                iAwakeCheckBox.isSelected = true
                iDestroyCheckBox.isSelected = true
                
                // 添加单选框切换监听器
                val radioListener = { _: java.awt.event.ActionEvent -> 
                    SwingUtilities.invokeLater {
                        updateDialogSize()
                    }
                }
                
                // 为所有单选框添加监听器
                serverRadio.addActionListener(radioListener)
                clientRadio.addActionListener(radioListener)
                shareRadio.addActionListener(radioListener)
                
                entityRadio.addActionListener {
                    interfacePanel.isVisible = true
                    group3Panel.isVisible = true
                    group4Panel.isVisible = false
                    SwingUtilities.invokeLater {
                        updateDialogSize()
                        SwingUtilities.invokeLater {
                            updateDialogSize()
                        }
                    }
                }
                
                componentRadio.addActionListener { 
                    interfacePanel.isVisible = true
                    group3Panel.isVisible = true
                    group4Panel.isVisible = false
                    SwingUtilities.invokeLater {
                        updateDialogSize()
                        SwingUtilities.invokeLater {
                            updateDialogSize()
                        }
                    }
                }
                systemRadio.addActionListener { 
                    interfacePanel.isVisible = false
                    group3Panel.isVisible = false
                    group4Panel.isVisible = false
                    SwingUtilities.invokeLater {
                        updateDialogSize()
                        SwingUtilities.invokeLater {
                            updateDialogSize()
                        }
                    }
                }
                handlerRadio.addActionListener {
                    interfacePanel.isVisible = false
                    group3Panel.isVisible = false
                    group4Panel.isVisible = true
                    SwingUtilities.invokeLater {
                        updateDialogSize()
                        SwingUtilities.invokeLater {
                            updateDialogSize()
                        }
                    }
                }
                
                childOfRadio.addActionListener(radioListener)
                componentOfRadio.addActionListener(radioListener)

                messageHandlerRadio.addActionListener(radioListener)
                messageSessionHandlerRadio.addActionListener(radioListener)
                messageLocationHandlerRadio.addActionListener(radioListener)
            }
            
            private fun updateDialogSize() {
                mainPanel.revalidate()
                mainPanel.repaint()
                pack()
                repaint()
            }

            override fun createCenterPanel(): JComponent {
                mainPanel = JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                }
                
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
                group2Panel.add(entityRadio)
                group2Panel.add(componentRadio)
                group2Panel.add(systemRadio)
                group2Panel.add(handlerRadio)
                mainPanel.add(group2Panel)
                
                // 第三组单选框面板
                group3Panel.add(JLabel("标签类型:            "))
                group3Panel.add(childOfRadio)
                group3Panel.add(componentOfRadio)
                group3Panel.add(JLabel("    标签值:"))
                group3Panel.add(tagValueField)
                mainPanel.add(group3Panel)

                // 第四组单选框面板
                group4Panel.add(JLabel("消息类型:            "))
                group4Panel.add(messageHandlerRadio)
                group4Panel.add(messageSessionHandlerRadio)
                group4Panel.add(messageLocationHandlerRadio)
                group4Panel.isVisible = false
                mainPanel.add(group4Panel)
                
                // 配置接口复选框面板
                val interfaceLabelPanel = JPanel(FlowLayout(FlowLayout.LEFT))
                mainPanel.add(interfaceLabelPanel)
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

            override fun doValidate(): ValidationInfo? {
                return if (nameTextField.text.trim().isEmpty()) {
                    ValidationInfo("请输入名称", nameTextField)
                } else {
                    null
                }
            }
        }

        if (dialog.showAndGet()) {
            val name = dialog.nameTextField.text.trim()
            
            // 获取选中的命名空间
            val namespace = when {
                dialog.serverRadio.isSelected -> "ET.Server"
                dialog.clientRadio.isSelected -> "ET.Client"
                else -> "ET"
            }
            
            // 获取选中的类型
            val type = when {
                dialog.entityRadio.isSelected -> "Entity"
                dialog.componentRadio.isSelected -> "Component"
                dialog.systemRadio.isSelected -> "System"
                else -> "Handler"
            }
            
            // 生成类的内容
            val classContent = buildString {
                appendLine("namespace $namespace")
                appendLine("\u007B")
                
                // 如果是Entity或Component类型，添加选中的接口和标签
                if (dialog.entityRadio.isSelected || dialog.componentRadio.isSelected) {
                    val tagValue = dialog.tagValueField.text.trim()
                    if (dialog.childOfRadio.isSelected) {
                        if (tagValue.isEmpty()) {
                            appendLine("    [ChildOf]")
                        } else {
                            appendLine("    [ChildOf(typeof($tagValue))]")
                        }
                    } else if (dialog.componentOfRadio.isSelected) {
                        if (tagValue.isEmpty()) {
                            appendLine("    [ComponentOf]")
                        } else {
                            appendLine("    [ComponentOf(typeof($tagValue))]")
                        }
                    }
                    
                    val interfaces = mutableListOf<String>()
                    if (dialog.iAwakeCheckBox.isSelected) interfaces.add("IAwake")
                    if (dialog.iUpdateCheckBox.isSelected) interfaces.add("IUpdate")
                    if (dialog.iLateUpdateCheckBox.isSelected) interfaces.add("ILateUpdateSystem")
                    if (dialog.iDestroyCheckBox.isSelected) interfaces.add("IDestroy")
                    if (dialog.iGetComponentSysCheckBox.isSelected) interfaces.add("IGetComponentSys")
                    if (dialog.iSerializeCheckBox.isSelected) interfaces.add("ISerialize")
                    if (dialog.iDeserializeCheckBox.isSelected) interfaces.add("IDeserialize")
                    
                    append("    public class ${if (dialog.entityRadio.isSelected) "$name" else "$name$type"} : Entity")
                    if (interfaces.isNotEmpty()) {
                        append(", ")
                        append(interfaces.joinToString(", "))
                    }
                    appendLine()
                    appendLine("    \u007B")
                    appendLine("        ")
                    appendLine("    \u007D")
                } else if (dialog.handlerRadio.isSelected) {
                    // 处理Handler类型
                    when {
                        dialog.messageHandlerRadio.isSelected -> {
                            appendLine("    [MessageHandler(SceneType.Demo)]") 
                            appendLine("    public class $name$type : MessageHandler<Scene, $name>")
                            appendLine("    \u007B")
                            appendLine("        protected override async ETTask Run(Scene scene, $name message)")
                            appendLine("        \u007B")
                            appendLine("            await ETTask.CompletedTask;")
                            appendLine("        \u007D")
                            appendLine("    \u007D")
                        }
                        dialog.messageSessionHandlerRadio.isSelected -> {
                            appendLine("    [MessageSessionHandler(SceneType.Realm)]")
                            val reversedName = if (name.contains("2")) {
                                val parts = name.split("2")
                                val suffix = if (parts[1].contains("_")) {
                                    "_" + parts[1].substringAfter("_") 
                                } else ""
                                val prefix = parts[1].substringBefore("_")
                                "${prefix}2${parts[0]}$suffix"
                            } else {
                                name
                            }
                            appendLine("    public class $name$type : MessageSessionHandler<$name, $reversedName>")
                            appendLine("    \u007B")
                            appendLine("        protected override async ETTask Run(Session session, $name request, $reversedName response)")
                            appendLine("        \u007B")
                            appendLine("            await ETTask.CompletedTask;")
                            appendLine("        \u007D")
                            appendLine("    \u007D")
                        }
                        else -> {
                            appendLine("    [MessageLocationHandler(SceneType.Map)]")
                            val reversedName = if (name.contains("2")) {
                                val parts = name.split("2")
                                val suffix = if (parts[1].contains("_")) {
                                    "_" + parts[1].substringAfter("_") 
                                } else ""
                                val prefix = parts[1].substringBefore("_")
                                "${prefix}2${parts[0]}$suffix"
                            } else {
                                name
                            }
                            appendLine("    public class $name$type : MessageLocationHandler<Unit, $name, $reversedName>")
                            appendLine("    \u007B")
                            appendLine("        protected override async ETTask Run(Unit unit, $name request, $reversedName response)")
                            appendLine("        \u007B")
                            appendLine("            await ETTask.CompletedTask;")
                            appendLine("        \u007D")
                            appendLine("    \u007D")
                        }
                    }
                } else {
                    // System类型
                    appendLine("    [EntitySystemOf(typeof($name))]")
                    appendLine("    public static partial class ${name}System")
                    appendLine()
                    appendLine("    \u007B")
                    appendLine("        ")
                    appendLine("    \u007D")
                }
                appendLine("\u007D")
            }
            
            // 创建新文件
            val psiFileFactory = PsiFileFactory.getInstance(project)
            val fileName = when {
                dialog.entityRadio.isSelected -> "$name.cs"
                else -> "$name$type.cs"
            }
            val file = psiFileFactory.createFileFromText(fileName, PlainTextFileType.INSTANCE, classContent)
            
            // 获取当前文件所在的目录
            val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
            
            // 判断是否选中的是目录
            val targetDirectory = if (virtualFile?.isDirectory == true) {
                virtualFile
            } else {
                virtualFile?.parent
            }
            
            // 在目录中创建新文件
            if (targetDirectory != null) {
                WriteCommandAction.runWriteCommandAction(project) {
                    val psiDirectory = PsiManager.getInstance(project).findDirectory(targetDirectory)
                    val newFile = psiDirectory?.add(file)
                    
                    // 打开新创建的文件
                    if (newFile != null) {
                        FileEditorManager.getInstance(project).openFile(newFile.containingFile.virtualFile, true)
                    }
                }
            }
        }
    }
}