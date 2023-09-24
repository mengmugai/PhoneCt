
#pragma once

// /* https://github.com/android/ndk/issues/1422 */
// #include <features.h>

#include <asm/unistd.h> /* For system call numbers. */

#define MAX_ERRNO 4095  /* For recognizing system call error returns. */

#define __bionic_asm_custom_entry(f)
#define __bionic_asm_custom_end(f)
#define __bionic_asm_function_type @function
#define __bionic_asm_custom_note_gnu_section()

/* Instead of including "<private/bionic_asm_{ARCH}.h>"s, which are private in
 * AOSP and not available in Android NDK, we copy what we need in them and paste
 * them into the following architecture-specific defines. This makes our
 * "bionic_asm.h" a standalone header file.
*/
#if defined(__aarch64__)

// #include <private/bionic_asm_arm64.h>
// == NOTE: code here is copied from "bionic_asm_arm64.h" ==
#define __bionic_asm_align 16
#undef __bionic_asm_function_type
#define __bionic_asm_function_type %function
// =========================================================

#elif defined(__arm__)

// #include <private/bionic_asm_arm.h>
#define __bionic_asm_align 0
#undef __bionic_asm_custom_entry
#undef __bionic_asm_custom_end
#define __bionic_asm_custom_entry(f) .fnstart
#define __bionic_asm_custom_end(f) .fnend
#undef __bionic_asm_function_type
#define __bionic_asm_function_type #function

#elif defined(__x86_64__)

// #include <private/bionic_asm_x86_64.h>
#define __bionic_asm_align 16

#endif


#define ENTRY_NO_DWARF(f) \
    .text; \
    .globl f; \
    .balign __bionic_asm_align; \
    .type f, __bionic_asm_function_type; \
    f: \
    __bionic_asm_custom_entry(f); \

#define ENTRY(f) \
    ENTRY_NO_DWARF(f) \
    .cfi_startproc \

#define END_NO_DWARF(f) \
    .size f, .-f; \
    __bionic_asm_custom_end(f) \

#define END(f) \
    .cfi_endproc; \
    END_NO_DWARF(f) \

/* Like ENTRY, but with hidden visibility. */
#define ENTRY_PRIVATE(f) \
    ENTRY(f); \
    .hidden f \

/* Like ENTRY_NO_DWARF, but with hidden visibility. */
#define ENTRY_PRIVATE_NO_DWARF(f) \
    ENTRY_NO_DWARF(f); \
    .hidden f \

#define __BIONIC_WEAK_ASM_FOR_NATIVE_BRIDGE(f) \
    .weak f; \

#define ALIAS_SYMBOL(alias, original) \
    .globl alias; \
    .equ alias, original

#define NOTE_GNU_PROPERTY() \
    __bionic_asm_custom_note_gnu_section()