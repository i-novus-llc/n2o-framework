import { newPath } from '../../constants/constants'
import CONFIG from '../../ci-config.json'

export function getFileLang(fileName) {
    const fileNameParts = fileName.split('.')

    return fileNameParts[fileNameParts.length - 1]
}

export const fetchProjectId = async (templateId) => {
    try {
        const response = await fetch(`${CONFIG.sandboxUrl}${newPath}/${templateId}/`)
        const projectId = await response.text()

        return { projectId }
    } catch (error) {
        console.warn(error)
    }
}
