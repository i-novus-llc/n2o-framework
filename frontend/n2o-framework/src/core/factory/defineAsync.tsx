import React, { ComponentType, lazy, Suspense } from 'react'

import { Spinner, SpinnerType } from '../../factoryComponents/Spinner'
import { logger } from '../utils/logger'

const ErrorComponent = () => (
    <div>
        {/* TODO: Подключить обработчик ошибок, для отображения заглушек */}
    </div>
)

const LoadingComponent = () => <Spinner type={SpinnerType.inline} minSpinnerTimeToShow={1000} loading />

export function defineAsync<
    P extends {},
    C extends object, // У большинства компонентов где-то по пути теряется тип ComponentType<P> из-за конектов к редаксу
    D extends { default: C },
    T extends D | C,
>(loader: () => Promise<T>): ComponentType<P> {
    const AsyncComponent = lazy(() => loader().then((module) => {
        if ('default' in module) {
            return module as { default: ComponentType<P> }
        }

        return { default: module as ComponentType<P> }
    }).catch((error) => {
        logger.error(error)

        return { default: ErrorComponent }
    })) as unknown as ComponentType<P>

    return (props: P) => (
        <Suspense fallback={<LoadingComponent />}>
            <AsyncComponent {...props} />
        </Suspense>
    )
}
