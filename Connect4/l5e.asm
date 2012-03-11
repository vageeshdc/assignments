.model small
.stack 100h
.data

prompt1 db '$'
counter db 1 dup(0)           ; ;
turn db 1 dup(0)              ; ; 
result db 1 dup(0)            ; ;
setting db 64 dup(0) 	      ; ;contains the array data!!
input db 5,5 dup('$')         ; ;contains the input
input_correct db 1
corX db 1 dup(0)
corY db 1 dup(0)
mode db 1 dup(0)
ar_index db 1 dup(0)
rw_index db 1 dup(0)
ct_index dw 1 dup(0)
pr_turn db 1 dup(0)
.code

PushAll macro 
	push ax
	push bx
	push cx
	push dx
	endm

PopAll macro
	pop dx
	pop cx
	pop bx
	pop ax
	endm 

SetData macro
     mov counter ,1h   
     mov turn,1h
     mov result,0h
     mov input[0],'$'
     mov input[1],'$'
     mov input[2],'$'
     mov input[3],'$'
     mov input[4],'$'
     mov input[5],'$'
     
     mov dl,0h
     lea si,setting
     
loop_reset:
    cmp dl,35h
    jge done_reset
    mov [si],dl     
    inc si
    inc dl
    jmp loop_reset
    
done_reset:
endm

DrawLine macro startX,startY,len,color
	local is_horizontal,is_vertical,is_diagonal,not_horizontal,is_diagonalRight,is_diagonalLeft,diagonalToLeft,diagonalToRight,endline
	PushAll

	mov ah,0ch 	; function 0Ch
	mov al,color
	mov cx, startX
	mov dx, startY
	mov bx, len 

	cmp mode,1
	jne not_horizontal
; Horizontal 
is_horizontal: 
	cmp bx,0h
	je endline
	int 10h
	inc cx
	dec bx
	jmp is_horizontal

; Not Horizontal 
not_horizontal:
	cmp mode,2
	jne diagonalToLeft
; Vertical 
is_vertical:
	cmp bx,0h
	je endline
	int 10h
	inc dx
	dec bx
	jmp is_vertical
; Diagonal 
diagonalToLeft:
	cmp mode,3
	jne diagonalToRight
is_diagonalLeft: 
	cmp bx,0h
	je endline
	int 10h
	inc cx
	inc dx
	dec bx
	jmp is_diagonalLeft
;Diagnoal  to right
diagonalToRight:
	cmp mode,4
	jne endline
is_diagonalRight: 
	cmp bx,0h
	je endline
	int 10h
	dec cx
	inc dx
	dec bx
	jmp is_diagonalRight
endline:
	PopAll
endm

ClearCommandLine macro
	PushAll
	mov ax,@data
	mov es,ax
	mov bp,offset prompt1	; ES:BP points to message
	mov ah,13h 		; function 13 - write string
	mov al,01h 		; attrib in bl,move cursor
	xor bh,bh 		; video page 0
	mov bl,5 		; attribute - magenta
	mov cx,10 		; length of string
	mov dh,25 		; row to put string
	mov dl,5 		; column to put string
	int 10h 		; call BIOS service
	PopAll
	endm

PrintCommandLine macro inputString
	PushAll
	mov ax,@data
	mov es,ax
	mov bp,offset inputString	; ES:BP points to message
	mov ah,13h 			; function 13 - write string
	mov al,01h 			; attrib in bl,move cursor
	xor bh,bh 			; video page 0
	mov bl,5 			; attribute - magenta
	mov cx,10 		; length of string
	mov dh,25 		; row to put string
	mov dl,5 		; column to put string
	int 10h 		; call BIOS service
	int 21h
	PopAll
	endm
	

start:
	mov ax,@data
	mov ds,ax

	mov ah,0h
	mov al,13h 	; mode = 13h 
	int 10h 	; call bios service

    ;SetData

	call DrawGrid
	;have to start to use the macro here in this aspect
	; has only the loop sand not macro

	mov result,0
	mov turn,1       
    mov counter,0

mainloop:
        cmp result ,0h
        jne gameover
    
	call DisplayPromptGetInput
	call CheckCorrectness
	cmp input_correct,1
	jne mainloop
	call AnalyseInput     ;gets the validity

    	jmp mainloop

gameover:
	mov ax,4C00h 	          ; exit to DOS
	int 21h


DisplayPromptGetInput proc
	
	mov al, 1
	mov bh, 0
	mov bl, 3
	mov cx, msg1end2 - offset msg2 ; calculate message size. 
	mov dl, 7d
	mov dh, 21d
	push cs
	pop es
	mov bp, offset msg2
	mov ah, 13h
	int 10h
	jmp msg1end2
	msg2 db "                                 "
	msg1end2:


mov al, 1
	mov bh, 0
	mov bl, 3
	mov cx, msg1end - offset msg1 ; calculate message size. 
	mov dl, 7
	mov dh, 21d
	push cs
	pop es
	mov bp, offset msg1
	mov ah, 13h
	int 10h
	jmp msg1end
	msg1 db " Move :"
	msg1end:
	 

mov ax,@data
mov ds,ax	 
	 mov dx, offset prompt1            ;Getting the input
    	mov ah, 09h
    	int 21h 
	 
	 mov dx, offset input           ;Getting the input
    	mov ah, 0ah
    	int 21h 
	 
	ret
DisplayPromptGetInput endp

CheckCorrectness proc
	 lea si,input
	cmp input[2],'p'
	jne incorrect_input

	mov al,turn
	add al,'0'
	cmp al,input[3] 
	jne incorrect_input 

	cmp input[4],' '
	jne incorrect_input

	cmp input[5],'0'
	jle incorrect_input
	cmp input[5],'7'
	jge incorrect_input
	
	mov input_correct,1
	ret 
incorrect_input: 
    cmp input[2],'q'
    jne other_msg
    mov input_correct,0
    call Quitgame
    jmp end_message
other_msg:
end_message:    
	mov input_correct,0
	ret 
CheckCorrectness endp

AnalyseInput proc 
    ;;here the logic to insert the data!!

    mov al,input[5]
    sub al,'1'
    mov ah,0h
		
    ; al now contains the col. number
    ; Move to the last row in that col. 
    add al,49d
    lea bx,setting  ; this stores all the data
    add bx,ax
    mov ah,6	    ; ah contains the row number
    
posfinder:
    ;mov mode ,3h
    ;DrawLine al,12,25,4
        mov dl,[bx]
        cmp dl,0h   
	je updatearr
    
	; This cell is filled. Move to the row about it
        sub bx,8h     
	; al keeps track of whether the array index is within bounds
        sub al,8h
	; Row number should decrease by 1
        sub ah,1h
    
        cmp al,0h
        jnl not_out_of_bounds
	; Display an error here.
	; If you came here, the whole col is filled.
        ret
not_out_of_bounds:
        jmp posfinder 


;;;;;;;;;;;;;save AH!,BX still has it!! ;;;;;;;;;;;;;;;;
updatearr:

    mov ct_index,bx
    mov rw_index,ah
    mov ar_index,al
	
	mov dl,turn
	mov pr_turn,dl
	
    	cmp dl,1h
    	jne turnisofp2
	    mov byte ptr [bx],dl
    	mov turn,2h
    	jmp turn_update_done
    
turnisofp2:

	mov byte ptr [bx],dl
	mov turn,1h
	jmp turn_update_done

temp01:
	jmp exitfun

turn_update_done:
    
    add counter,1h
    ;set ggame counter for finding draw
    
    mov corX,ah
    mov al,input[5]
    sub al,'0'
    mov corY,al
    
    call UpdateFig
    
call resultgame
    

resultdraw:
    cmp counter,36d
    jl exitfun 
    call drawgame
   ;;this checks for draw

exitfun:

ret
AnalyseInput endp

;function tp update the UI
Updatefig proc

    ;calucation to get the coordinates from grid to display
    mov ax,corY
    mov ah,0h
    sub al,1h
    mov cl,25d
    mul cl
    add al,85d
    mov cx,ax
    
    mov ax,corX
    mov ah,0h
    sub al,1h
    mov dl,25d
    mul dl
    add al,10d
    mov dx,ax
    
    mov mode,3
   
    
  ;  
    cmp turn,1h
    jne player2_turn
    mov bl,3h
    jmp p2draw
    
    player2_turn:
    mov bl,6h
    
    p2draw:
    DrawLine cx,dx,25,bl

    mov ax,corY
    mov ah,0h
    sub al,1h
    mov cl,25d
    mul cl
    add al,85d
    add al,25d
    
    mov cx,ax
    
    mov ax,corX
    mov ah,0h
    sub al,1h
    mov dl,25d
    mul dl
    add al,10d
   mov dx,ax
    
    mov mode,4        
    DrawLine cx,dx,25,bl
  ;  
	ret

Updatefig endp
;;;;;;;;;;;;;;;;;;;;fun 4;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
drawgame proc

mov al, 1
	mov bh, 0
	mov bl, 3
	mov cx, msgDrawend - offset msgDr ; calculate message size. 
	mov dl, 7
	mov dh, 21d
	push cs
	pop es
	mov bp, offset msgDr
	mov ah, 13h
	int 10h
	jmp msgDrawend
	msgDr db " Draw !"
	msgDrawend:
    
    lea si,result       ;update result to stop
    mov al,1h
    mov [si],al 
    
ret

drawgame endp
;;;;;;;;;;;;;;;;;;;fun 5;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
p1wingame proc

mov al, 1
	mov bh, 0
	mov bl, 3
	mov cx, msgP1end - offset msgP1 ; calculate message size. 
	mov dl, 7
	mov dh, 21d
	push cs
	pop es
	mov bp, offset msgP1
	mov ah, 13h
	int 10h
	jmp msgP1end
	msgP1 db " Player 1 wins!"
	msgP1end: 
	
	lea si,result       ;update result to stop
    mov al,1h
    mov [si],al 
    
ret
p1wingame endp

;;;;;;;;;;;;;;;;;;;fun 6;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
p2wingame proc

mov al, 1
	mov bh, 0
	mov bl, 3
	mov cx, msgP2end - offset msgP2 ; calculate message size. 
	mov dl, 7
	mov dh, 21d
	push cs
	pop es
	mov bp, offset msgP2
	mov ah, 13h
	int 10h
	jmp msgP2end
	msgP2 db " Player 2 wins!"
	msgP2end: 
    
    lea si,result       ;update result to stop
    mov al,1h
    mov [si],al        
    
ret
p2wingame endp

DrawGrid proc 

	mov ah,0Ch 	; function 0Ch
	mov al,4 	; color 4 - red


	mov cx,85
	mov dx,10

	loop1a:
	cmp dx,161
	jge sett
	mov cx,85
	loop22a:
	cmp cx,236
	jge loopca
	int 10h
	inc cx
	jmp loop22a
	loopca:
	add  dx,25
	jmp loop1a


	sett:
	mov cx,85
	mov dx,10

	loop11:
	cmp cx,236
	jge endo
	mov dx,10
	loop22:
	cmp dx,161
	jge loopc
	int 10h
	inc dx
	jmp loop22
	loopc:
	add  cx,25
	jmp loop11
endo:
	ret
DrawGrid endp

resultgame proc

    mov dh,0h ; count for p1
    mov dl,0h ; count for p2
    
    mov al,input[5]
    sub al,'1'
    lea si,setting
    mov ah,0h
    add si,ax
    add si,30
    mov bl,8h
    mov cl,ar_index
    mov si,ct_index
    mov bx,8h
    mov al,0
                        
    p1col: 
          inc al
          add si,bx
          add cl,bl
          cmp setting[si],1h
          je p1col
                        
 p1rowtest:                       
 mov si,ct_index
         
    p1col1:
          inc al
          sub si,bx
          cmp setting[si],1h
          je p1col1
          cmp al,4h
          jne  p2coltest
          call p2wingame
          ret
      
p2coltest:                        
    mov al,0
                        
    p2col:
                    
          inc al
          add si,bx
          cmp setting[si],2h
          je p2col
    p2rowtest:
    p2col2:
                     
          inc al
          sub si,bx
          cmp setting[si],2h
          je p2col2
                        
     row_res:                       
     cmp al,4h
     jne findrow
     call p1wingame
     ret
findrow:

    mov al,rw_index
    dec al
    mov bh,6h
    mul bh
    mov dh,0h ; count for p1
    mov dl,0h ; count for p2
    lea si,setting
    mov ah,0h
    add si,ax
    mov bl,6h
    
    mov si,ct_index
    
    mov dh,-1
                        
    llabelp11:                        
           inc dh
           dec si
           mov al,[si]
           cmp al,2
           je llabelp11
     
    mov si,ct_index
                                                                      
    llabelp:                        
            inc dh                  
            inc si
            mov al,setting[si]
            cmp al,2
            je llabelp
        
     cmp dh,4    
     jne sumrowp1 
     
     call p2wingame
     
     jmp finddiagl   
 
sumrowp1:
    mov si,ct_index    
    mov ah,-1
                        
    llabel2:                       
           inc ah
           dec si
           mov al,[si]
           cmp al,1
           je llabel2
     
    mov si,ct_index
                                                                      
    llabel3:                        
            inc ah                  
            inc si
            mov al,[si]
            cmp al,1
            je llabel3
        
     cmp ah,4    
     jne finddiagl
     call p1wingame
    
finddiagl:
    
    leftdiag:
    mov bx,9h
    mov si,ct_index
                        
    mov ah,-1
              
    labell4:                       
          inc ah
          dec si
          sub si,bx
          cmp setting[si],1
          je labell4
                        
          mov si,ct_index
                                                     
     labell5:                        
          inc ah                
          inc si
          add si,bx
          cmp setting[si],1
          je labell5
        
      cmp ah,4
          jne checkp1ld
          call p1wingame
          ret
                        
checkp1ld:              
       mov bx,7h                        
       mov si,ct_index
       mov ah,-1
       
       labell6:                        
            inc ah
            dec si
            add si,bx
            cmp setting[si],1
            je labell6
               
            mov si,ct_index
                                                                      
        labell7:                        
             inc ah                 
             inc si
             sub si,bx
             cmp setting[si],1
             je labell7
        
         cmp ah,4
         jne p2alldiag
         call p1wingame
p2alldiag:    

    mov bx,9h
    mov si,ct_index
    mov ah,-1
                        
    p2ldiag:                       
         inc ah
         dec si
         sub si,bx
         cmp setting[si],2
         je p2ldiag
     
         mov si,ct_index
                                                                      
     p2ldiag1:                        
         inc ah                
         inc si
         add si,bx
         cmp setting[si],2
         je p2ldiag1
        
          cmp ah,4
          jne checkp2ld
          call p2wingame
       ret
                        
checkp2ld:              
       mov bx,7h                        
       mov si,ct_index
       mov ah,-1
                        
       p2rdiag:                        
             inc ah
             dec si
             add si,bx
             cmp setting[si],2
             je p2rdiag
                        
             mov si,ct_index
                                                                      
       p2rdiag1:                        
             inc ah                 
             inc si
             sub si,bx
             cmp setting[si],2
             je p2rdiag1
        
       cmp ah,4
       jne exitfunc
       call p2wingame
       
exitfunc:
    ret
resultgame endp

Quitgame proc
    
    lea si,result       ;update result to stop
    mov al,1h
    mov [si],al
    ret
Quitgame endp

end start

