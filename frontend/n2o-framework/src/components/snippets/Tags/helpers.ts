// Функция обрезки текста в тэгах
export const truncate = (text: string, maxLength?: number): string => {
    if (!maxLength || text.length <= maxLength) { return text }

    return `${text.slice(0, maxLength)}...`
}
