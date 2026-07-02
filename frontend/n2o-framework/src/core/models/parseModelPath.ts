import { FullModelPath, ModelLink, ModelPath, ModelPrefix } from './types'

export const parseModelPath = (modelPath: FullModelPath | ModelPath): ModelLink => {
    const match = modelPath.match(/^(models\.)?(resolve|multi|datasource|filter|edit)\['(.+)'](\[\d+])?/)

    if (!match) { throw new Error(`Incorrect model path: ${modelPath}`) }

    const [, __, prefix, id, index] = match as ['models.' | undefined, string, ModelPrefix, string, `[${number}]` | undefined]

    return { prefix, id, index: index ? parseInt(index.slice(1, -1), 10) : undefined }
}
