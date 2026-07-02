import { FullModelPath } from '../../../core/models/types'

export type Handler = (isChanged: (link: FullModelPath) => boolean) => void

export type Predicate = (action: unknown) => boolean

export type Subscriber = {
    predicate?: Predicate
    handler: Handler
}
