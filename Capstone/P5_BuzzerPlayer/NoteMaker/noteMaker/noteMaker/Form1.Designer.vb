<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class Form1
    Inherits System.Windows.Forms.Form

    'Form은 Dispose를 재정의하여 구성 요소 목록을 정리합니다.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Windows Form 디자이너에 필요합니다.
    Private components As System.ComponentModel.IContainer

    '참고: 다음 프로시저는 Windows Form 디자이너에 필요합니다.
    '수정하려면 Windows Form 디자이너를 사용하십시오.  
    '코드 편집기를 사용하여 수정하지 마십시오.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.TextNote = New System.Windows.Forms.TextBox()
        Me.ListLen = New System.Windows.Forms.ListBox()
        Me.TextLen = New System.Windows.Forms.TextBox()
        Me.ButtonRest = New System.Windows.Forms.Button()
        Me.RadioSharp = New System.Windows.Forms.RadioButton()
        Me.RadioFlat = New System.Windows.Forms.RadioButton()
        Me.RadioNone = New System.Windows.Forms.RadioButton()
        Me.TextSharp = New System.Windows.Forms.TextBox()
        Me.TextFlat = New System.Windows.Forms.TextBox()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.ButtonBar = New System.Windows.Forms.Button()
        Me.Label3 = New System.Windows.Forms.Label()
        Me.Label4 = New System.Windows.Forms.Label()
        Me.CheckBass = New System.Windows.Forms.CheckBox()
        Me.SuspendLayout()
        '
        'TextNote
        '
        Me.TextNote.Location = New System.Drawing.Point(69, 462)
        Me.TextNote.Name = "TextNote"
        Me.TextNote.Size = New System.Drawing.Size(648, 21)
        Me.TextNote.TabIndex = 0
        '
        'ListLen
        '
        Me.ListLen.FormattingEnabled = True
        Me.ListLen.ItemHeight = 12
        Me.ListLen.Items.AddRange(New Object() {"32", "16", "8", "4", "2", "1"})
        Me.ListLen.Location = New System.Drawing.Point(597, 37)
        Me.ListLen.Name = "ListLen"
        Me.ListLen.Size = New System.Drawing.Size(120, 88)
        Me.ListLen.TabIndex = 1
        '
        'TextLen
        '
        Me.TextLen.Location = New System.Drawing.Point(597, 147)
        Me.TextLen.Name = "TextLen"
        Me.TextLen.Size = New System.Drawing.Size(120, 21)
        Me.TextLen.TabIndex = 3
        Me.TextLen.Text = "4"
        '
        'ButtonRest
        '
        Me.ButtonRest.Location = New System.Drawing.Point(597, 244)
        Me.ButtonRest.Name = "ButtonRest"
        Me.ButtonRest.Size = New System.Drawing.Size(120, 23)
        Me.ButtonRest.TabIndex = 4
        Me.ButtonRest.Text = "Rest"
        Me.ButtonRest.UseVisualStyleBackColor = True
        '
        'RadioSharp
        '
        Me.RadioSharp.AutoSize = True
        Me.RadioSharp.Location = New System.Drawing.Point(652, 192)
        Me.RadioSharp.Name = "RadioSharp"
        Me.RadioSharp.Size = New System.Drawing.Size(29, 16)
        Me.RadioSharp.TabIndex = 5
        Me.RadioSharp.TabStop = True
        Me.RadioSharp.Text = "#"
        Me.RadioSharp.UseVisualStyleBackColor = True
        '
        'RadioFlat
        '
        Me.RadioFlat.AutoSize = True
        Me.RadioFlat.Location = New System.Drawing.Point(687, 192)
        Me.RadioFlat.Name = "RadioFlat"
        Me.RadioFlat.Size = New System.Drawing.Size(30, 16)
        Me.RadioFlat.TabIndex = 6
        Me.RadioFlat.TabStop = True
        Me.RadioFlat.Text = "b"
        Me.RadioFlat.TextAlign = System.Drawing.ContentAlignment.BottomLeft
        Me.RadioFlat.UseVisualStyleBackColor = True
        '
        'RadioNone
        '
        Me.RadioNone.AutoSize = True
        Me.RadioNone.Location = New System.Drawing.Point(597, 192)
        Me.RadioNone.Name = "RadioNone"
        Me.RadioNone.Size = New System.Drawing.Size(51, 16)
        Me.RadioNone.TabIndex = 7
        Me.RadioNone.TabStop = True
        Me.RadioNone.Text = "none"
        Me.RadioNone.UseVisualStyleBackColor = True
        '
        'TextSharp
        '
        Me.TextSharp.Location = New System.Drawing.Point(622, 290)
        Me.TextSharp.Name = "TextSharp"
        Me.TextSharp.Size = New System.Drawing.Size(95, 21)
        Me.TextSharp.TabIndex = 8
        '
        'TextFlat
        '
        Me.TextFlat.Location = New System.Drawing.Point(623, 317)
        Me.TextFlat.Name = "TextFlat"
        Me.TextFlat.Size = New System.Drawing.Size(94, 21)
        Me.TextFlat.TabIndex = 8
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Location = New System.Drawing.Point(597, 298)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(19, 12)
        Me.Label1.TabIndex = 9
        Me.Label1.Text = "# :"
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Location = New System.Drawing.Point(597, 320)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(20, 12)
        Me.Label2.TabIndex = 9
        Me.Label2.Text = "b :"
        '
        'ButtonBar
        '
        Me.ButtonBar.Location = New System.Drawing.Point(597, 369)
        Me.ButtonBar.Name = "ButtonBar"
        Me.ButtonBar.Size = New System.Drawing.Size(120, 23)
        Me.ButtonBar.TabIndex = 10
        Me.ButtonBar.Text = "Bar"
        Me.ButtonBar.UseVisualStyleBackColor = True
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Font = New System.Drawing.Font("MS Gothic", 9.75!, CType((System.Drawing.FontStyle.Bold Or System.Drawing.FontStyle.Italic), System.Drawing.FontStyle), System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label3.ForeColor = System.Drawing.Color.FromArgb(CType(CType(72, Byte), Integer), CType(CType(176, Byte), Integer), CType(CType(155, Byte), Integer))
        Me.Label3.Location = New System.Drawing.Point(619, 495)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(95, 13)
        Me.Label3.TabIndex = 11
        Me.Label3.Text = "by 20111638"
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Font = New System.Drawing.Font("MS Gothic", 15.0!, CType((System.Drawing.FontStyle.Bold Or System.Drawing.FontStyle.Italic), System.Drawing.FontStyle), System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label4.ForeColor = System.Drawing.Color.FromArgb(CType(CType(243, Byte), Integer), CType(CType(177, Byte), Integer), CType(CType(97, Byte), Integer))
        Me.Label4.Location = New System.Drawing.Point(565, 518)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(152, 20)
        Me.Label4.TabIndex = 11
        Me.Label4.Text = "in Team Mango"
        '
        'CheckBass
        '
        Me.CheckBass.AutoSize = True
        Me.CheckBass.Location = New System.Drawing.Point(599, 214)
        Me.CheckBass.Name = "CheckBass"
        Me.CheckBass.Size = New System.Drawing.Size(53, 16)
        Me.CheckBass.TabIndex = 12
        Me.CheckBass.Text = "Bass"
        Me.CheckBass.UseVisualStyleBackColor = True
        '
        'Form1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(7.0!, 12.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(759, 550)
        Me.Controls.Add(Me.CheckBass)
        Me.Controls.Add(Me.Label4)
        Me.Controls.Add(Me.Label3)
        Me.Controls.Add(Me.ButtonBar)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.TextFlat)
        Me.Controls.Add(Me.TextSharp)
        Me.Controls.Add(Me.RadioNone)
        Me.Controls.Add(Me.RadioFlat)
        Me.Controls.Add(Me.RadioSharp)
        Me.Controls.Add(Me.ButtonRest)
        Me.Controls.Add(Me.TextLen)
        Me.Controls.Add(Me.ListLen)
        Me.Controls.Add(Me.TextNote)
        Me.Name = "Form1"
        Me.Text = "NoteMaker"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents TextNote As System.Windows.Forms.TextBox
    Friend WithEvents ListLen As System.Windows.Forms.ListBox
    Friend WithEvents TextLen As System.Windows.Forms.TextBox
    Friend WithEvents ButtonRest As System.Windows.Forms.Button
    Friend WithEvents RadioSharp As System.Windows.Forms.RadioButton
    Friend WithEvents RadioFlat As System.Windows.Forms.RadioButton
    Friend WithEvents RadioNone As System.Windows.Forms.RadioButton
    Friend WithEvents TextSharp As System.Windows.Forms.TextBox
    Friend WithEvents TextFlat As System.Windows.Forms.TextBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents ButtonBar As System.Windows.Forms.Button
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents Label4 As System.Windows.Forms.Label
    Friend WithEvents CheckBass As System.Windows.Forms.CheckBox

End Class
