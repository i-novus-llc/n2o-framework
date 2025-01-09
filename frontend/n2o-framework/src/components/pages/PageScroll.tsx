import React, {
    createContext,
    useState,
    useEffect,
    useMemo,
    useContext,
    ReactNode,
    MutableRefObject,
    ComponentType,
    FC,
} from 'react'
import omit from 'lodash/omit'
import get from 'lodash/get'
import { connect } from 'react-redux'

import { setPageScrolling } from '../../ducks/pages/store'
import { Metadata } from '../../ducks/pages/Pages'
import { State } from '../../ducks/State'
import { makePageScrollByIdSelector } from '../../ducks/pages/selectors'
import { ValidationResult } from '../../core/validation/types'

import { DefaultPageProps } from './types'

type Ref = MutableRefObject<null | HTMLElement>

export function getTopElement(refs: Ref[]) {
    const closest = refs.reduce(
        (acc, ref, index) => {
            const { top } = ref.current?.getBoundingClientRect() || { top: Number.MAX_VALUE }

            if (top < acc.closestDistance) {
                return { closestIndex: index, closestDistance: top }
            }

            return acc
        },
        { closestIndex: -1, closestDistance: Number.MAX_VALUE },
    )

    return refs[closest.closestIndex]
}

export const PageScrollContext = createContext<{
    collect(id: string, ref: Ref): void;
    remove(id: string): void;
}>({ collect: (id: string, ref: Ref) => {}, remove: (id: string) => {} })

export const PageScrollProvider = ({
    children,
    onScrollEnd,
    scroll = false,
}: {
    children: ReactNode
    onScrollEnd?(): void
    scroll: boolean
}) => {
    const [refs, setRef] = useState<Record<string, Ref>>({})
    const [scrollTimeout, setScrollTimeout] = useState<NodeJS.Timeout | null>(null)

    const collect = (id: string, ref: Ref) => { setRef(prevRefs => ({ ...prevRefs, [id]: ref })) }
    const remove = (id: string) => { setRef(prevRefs => omit(prevRefs, id)) }

    const context = useMemo(() => ({ collect, remove }), [])

    useEffect(() => {
        if (scroll) {
            const topElement = getTopElement(Object.values(refs))
            const current = get(topElement, 'current', null)

            if (current) {
                current.scrollIntoView({ behavior: 'smooth', block: 'start' })
            }

            if (scrollTimeout) { clearTimeout(scrollTimeout) }

            const timeout = setTimeout(() => {
                onScrollEnd?.()
            }, 0)

            setScrollTimeout(timeout)
        }

        return () => {
            if (scrollTimeout) { clearTimeout(scrollTimeout) }
        }
    }, [scroll])

    return <PageScrollContext.Provider value={context}>{children}</PageScrollContext.Provider>
}

const mapStateToProps = (state: State, { metadata }: { metadata: Metadata }) => {
    const { id: pageId } = metadata

    return { scroll: pageId ? makePageScrollByIdSelector(pageId)(state) : false }
}

export function PageScrollHOC<Props>(Component: ComponentType<Props>) {
    function Wrapper(props: Props & DefaultPageProps) {
        const { dispatch, metadata, scroll } = props
        const { id: pageId } = metadata

        const onScrollEnd = () => dispatch(setPageScrolling(pageId, false))

        return (
            <PageScrollProvider scroll={scroll} onScrollEnd={onScrollEnd}>
                <Component {...props} />
            </PageScrollProvider>
        )
    }

    return connect(mapStateToProps)(Wrapper as FC<DefaultPageProps>)
}

export function useScrollToFirstInvalid(
    scrollRef: Ref,
    id: string,
    message?: { message?: ValidationResult, validationClass?: string | false },
) {
    const scrollContext = useContext(PageScrollContext)
    const { collect, remove } = scrollContext

    useEffect(() => {
        if (scrollRef?.current) {
            if (message) {
                collect(id, scrollRef)

                return
            }

            remove(id)
        }
    }, [scrollRef, message])
}
