export async function copyTextToClipboard(text?: string | null) {
    try {
        if (text) {
            await navigator.clipboard.writeText(text)
            // eslint-disable-next-line no-console
            console.log('Текст успешно скопирован в буфер обмена.')
        }
    } catch (error) {
        console.error('Не удалось скопировать текст:', error)
    }
}
