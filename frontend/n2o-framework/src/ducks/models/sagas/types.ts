import { ModelLink } from '../../../core/models/types'

export type Handler = (keys: ModelLink[]) => void

export type Predicate = (action: unknown) => boolean

export type Subscriber = {
    predicate?: Predicate
    handler: Handler
}
