import { newPath } from '../../constants/constants'
import CONFIG from '../../ci-config.json'

export function getFileLang(fileName) {
    const fileNameParts = fileName.split('.')

    return fileNameParts[fileNameParts.length - 1]
}

export const openProjectWithRedirect = async (templateId) => {
    try {
        const response = await fetch(`${CONFIG.sandboxUrl}${newPath}/${templateId}/`)

        if (response.ok && response.redirected) {
            return { redirectUrl: response.url }
        }
    } catch (error) {
        console.warn(error)
    }
}
