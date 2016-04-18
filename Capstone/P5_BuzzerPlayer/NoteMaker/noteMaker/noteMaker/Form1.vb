Option Explicit On

Public Class Form1

    Public Const numNotes = 30

    Dim noteLog As New NoteLog()

    Dim first As Integer = 20
    Dim notes(numNotes) As PictureBox
    Dim octave = 0
    Dim notelist = "ABCDEFG"

    Dim noteWidth As Integer = 500
    Dim noteHeight As Integer = 10
    Dim noteTop As Integer = 50
    Dim noteLeft As Integer = 50
    Dim noteBottom As Integer = noteTop + noteHeight * numNotes

    Private Sub FocusTextNote() Handles Me.Click, Me.KeyDown
        TextNote.Focus()
        TextNote.SelectionStart = TextNote.TextLength
    End Sub

    Public Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load

        For i = LBound(notes) To UBound(notes)
            notes(i) = New PictureBox()
            notes(i).Width = noteWidth
            notes(i).Height = noteHeight
            notes(i).Left = noteLeft
            notes(i).Top = noteTop + noteHeight * i
            notes(i).BackColor = Color.White
            notes(i).Visible = True
            notes(i).Cursor = Cursors.Hand
            notes(i).Show()

            AddHandler notes(i).MouseClick, AddressOf onNoteClick
            AddHandler notes(i).MouseMove, AddressOf onNoteMouseMove

            Controls.Add(notes(i))

            If i Mod 2 = first Mod 2 Then
                If first - 8 <= i And i <= first Then
                    notes(i).BackgroundImage = My.Resources.line
                Else
                    notes(i).BackgroundImage = My.Resources.line2
                End If

                notes(i).BackgroundImageLayout = ImageLayout.Zoom
            End If

        Next

        FocusTextNote()
    End Sub

    Private Sub onNoteClick(sender As Object, e As MouseEventArgs)

        For i = LBound(notes) To UBound(notes)
            If sender.Equals(notes(i)) Then
                Dim base = first + 2
                If CheckBass.Checked Then base -= 12

                Dim pitch = base - i
                Dim oct = 0

                While (pitch < 0)
                    pitch += 7
                    oct -= 1
                End While

                While (7 <= pitch)
                    pitch -= 7
                    oct += 1
                End While

                Dim octDif = oct - octave
                octave = oct

                While 0 < octDif
                    octDif -= 1
                    TextNote.Text += "u"
                End While

                While octDif < 0
                    octDif += 1
                    TextNote.Text += "d"
                End While

                pitch += 2
                If 7 <= pitch Then pitch -= 7
                TextNote.Text += notelist(pitch)

                Dim sig = ""

                For j = 0 To TextSharp.TextLength - 1
                    Dim key = TextSharp.Text.ElementAt(j)
                    If key.Equals(notelist(pitch)) Then sig = "#"
                Next

                For j = 0 To TextFlat.TextLength - 1
                    Dim key = TextFlat.Text.ElementAt(j)
                    If key.Equals(notelist(pitch)) Then sig = "b"
                Next

                If RadioSharp.Checked Then sig = "#"
                If RadioFlat.Checked Then sig = "b"

                TextNote.Text += sig
                TextNote.Text += TextLen.Text

                noteLog.Add(i, e.X)
            End If
        Next

        noteLog.Draw(notes)
        FocusTextNote()
    End Sub

    Private Sub ListBox1_SelectedIndexChanged(sender As Object, e As EventArgs) Handles ListLen.SelectedIndexChanged
        If 0 <= ListLen.SelectedIndex Then
            TextLen.Text = ListLen.Items(ListLen.SelectedIndex).ToString
        End If

        FocusTextNote()
    End Sub

    Private Sub ButtonRest_Click(sender As Object, e As EventArgs) Handles ButtonRest.Click
        TextNote.Text += "R" + TextLen.Text
        FocusTextNote()
    End Sub

    Private Sub ButtonBar_Click(sender As Object, e As EventArgs) Handles ButtonBar.Click
        noteLog.Reset(notes)
        TextNote.Text += " "
        FocusTextNote()
    End Sub

    Private Sub onNoteMouseMove(sender As Object, e As MouseEventArgs)
        For i = LBound(notes) To UBound(notes)
            If notes(i).Equals(sender) Then
                noteLog.SetCursor(i, e.X)
            End If
        Next

        noteLog.Draw(notes)
        FocusTextNote()
    End Sub

    Private Sub TextNote_KeyUp(sender As Object, e As KeyEventArgs) Handles TextNote.KeyUp
        If e.KeyCode = Keys.Up Then
            ListLen.SelectedIndex = Math.Max(0, ListLen.SelectedIndex - 1)
            FocusTextNote()
        ElseIf e.KeyCode = Keys.Down Then
            ListLen.SelectedIndex = Math.Min(ListLen.Items.Count - 1, ListLen.SelectedIndex + 1)
            FocusTextNote()
        ElseIf e.KeyCode = Keys.Space Then
            noteLog.Reset(notes)
        End If
    End Sub

End Class


Class NoteLog

    Private Const numLog = 10
    Private log(numLog) As LogElem
    Private cursor As LogElem

    Public Sub New()
        For i = LBound(log) To UBound(log)
            log(i) = New LogElem()
        Next

        cursor = New LogElem
    End Sub

    Public Sub Add(id As Integer, x As Integer)
        For i = UBound(log) To 1 Step -1
            log(i).SetValue(log(i - 1))
        Next

        log(0).SetValue(id, x, True)
    End Sub

    Public Sub SetCursor(id As Integer, x As Integer)
        cursor.SetValue(id, x, True)
    End Sub

    Public Sub Reset(ByRef notes() As PictureBox)
        For i = LBound(log) To UBound(log)
            log(i).m_enable = False
        Next

        Draw(notes)
    End Sub

    Public Sub Draw(ByRef notes() As PictureBox)
        For i = LBound(notes) To UBound(notes)
            notes(i).Refresh()
        Next

        For i = LBound(log) To UBound(log)
            If log(i).m_enable Then
                log(i).Draw(notes, Brushes.Black)
            End If
        Next

        If cursor.m_enable Then
            cursor.Draw(notes, Brushes.Blue)
        End If
    End Sub

    Class LogElem
        Public Sub New()
            m_enable = False
        End Sub

        Public Sub SetValue(id As Integer, x As Integer, enable As Boolean)
            m_id = id
            m_x = x
            m_enable = enable
        End Sub

        Public Sub SetValue(e As LogElem)
            SetValue(e.m_id, e.m_x, e.m_enable)
        End Sub

        Public Sub Draw(ByRef notes() As PictureBox, ByRef brush As Brush)
            Dim note As PictureBox = notes(m_id)
            Dim grpNote As Graphics = note.CreateGraphics
            Dim h As Integer = (note.Height - 10) / 2

            grpNote.FillEllipse(brush, m_x, h, 10, 10)
            grpNote.Dispose()
        End Sub

        Public m_id As Integer
        Public m_x As Integer
        Public m_enable As Boolean

    End Class
End Class
