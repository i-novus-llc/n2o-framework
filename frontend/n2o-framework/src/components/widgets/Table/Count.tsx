import React from 'react'

import { Spinner, SpinnerType } from '../../../factoryComponents/Spinner'
import { usePrevious } from '../../../utils/usePrevious'
import { FactoryStandardButton } from '../../buttons/FactoryStandardButton'

export interface CountProps {
    count?: string | number | null
    visible: boolean
    showCountButton: boolean
    onClick?(): void
    loading?: boolean
}

function Counter({ loading, count }: Pick<CountProps, 'loading' | 'count'>) {
    const prevCount = usePrevious(count)
    const showLoading = loading && (prevCount !== count)

    return (
        <div className="pagination__total__counter">
            {showLoading && <Spinner showDelayMs={0} type={SpinnerType.cover} loading />}
            {!showLoading && <div>{count}</div>}
        </div>
    )
}

export function Count({ onClick, count, visible, loading, showCountButton }: CountProps) {
    if (!visible) { return null }

    if (count) {
        return (
            <section className="pagination__total__text">
                <div>Всего записей:</div>
                <Counter count={count} loading={loading} />
            </section>
        )
    }

    if (!showCountButton) { return null }

    return (
        <FactoryStandardButton onClick={onClick} className="pagination__total">
            <span className="title">Узнать количество записей</span>
        </FactoryStandardButton>
    )
}
