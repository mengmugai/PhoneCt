/* Generated by gensyscalls.py. Do not edit. */

#include "private/bionic_asm.h"

ENTRY_PRIVATE(___close)
    movl    $__NR_close, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(___close)

ENTRY_PRIVATE(_lseek)
    movl    $__NR_lseek, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_lseek)

ENTRY_PRIVATE(__openat)
    movq    %rcx, %r10
    movl    $__NR_openat, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(__openat)

ENTRY_PRIVATE(_read)
    movl    $__NR_read, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_read)

ENTRY_PRIVATE(__faccessat)
movl    $__NR_faccessat, %eax
        syscall
cmpq    $-MAX_ERRNO, %rax
        jb      1f
negl    %eax
        movl    %eax, %edi
        call    ___set_errno_internal
1:
ret
END(__faccessat)

ENTRY_PRIVATE(_write)
    movl    $__NR_write, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_write)


ENTRY_PRIVATE(_ptrace)
    movl    $__NR_ptrace, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_ptrace)

ENTRY_PRIVATE(_readlinkat)
    movq    %rcx, %r10
    movl    $__NR_readlinkat, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_readlinkat)

ENTRY_PRIVATE(_nanosleep)
    movl    $__NR_nanosleep, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_nanosleep)

ENTRY_PRIVATE(_inotify_init1)
    movl    $__NR_inotify_init1, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_inotify_init1)

ENTRY_PRIVATE(_inotify_add_watch)
    movl    $__NR_inotify_add_watch, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(_inotify_add_watch)

ENTRY_PRIVATE(_inotify_rm_watch)
    movl    $__NR_inotify_rm_watch, %eax
    syscall
    cmpq    $-MAX_ERRNO, %rax
    jb      1f
    negl    %eax
    movl    %eax, %edi
    call    ___set_errno_internal
1:
    ret
END(inotify_rm_watch)