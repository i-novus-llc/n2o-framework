import { ModelLink } from '../../../core/models/types'

export type Handler = (isChanged: (link: ModelLink) => boolean) => void

export type Predicate = (action: unknown) => boolean

export type Subscriber = {
    predicate?: Predicate
    handler: Handler
}
