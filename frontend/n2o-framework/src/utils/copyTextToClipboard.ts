import { logger } from '../core/utils/logger'

export enum CopyMessage {
    Empty = 'Текст для копирования отсутствует',
    Success = 'Текст скопирован в буфер',
    Error = 'Не удалось скопировать текст',
}

export async function copyTextToClipboard(text?: string | null): Promise<boolean> {
    if (!text) {
        logger.warn(CopyMessage.Empty)

        return false
    }

    try {
        await navigator.clipboard.writeText(text)
        logger.log(CopyMessage.Success)

        return true
    } catch (error) {
        logger.error(`${CopyMessage.Empty}: ${error}`)

        return false
    }
}
