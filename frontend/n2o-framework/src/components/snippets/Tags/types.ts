import { RefObject } from 'react'

export { type TagProps } from '@i-novus/n2o-components/lib/display/Tag'

export type TagsMeta = Array<{ id: string | number, value: string, isSummary?: boolean, enabled?: boolean }>

export interface TagsProps {
    tags: TagsMeta
    maxTagTextLength?: number
    onTagRemove?(id: string | number): void
    onClick?(): void
    className?: string
    ref?: RefObject<HTMLDivElement>
}
