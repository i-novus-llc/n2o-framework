import { useContext, createContext } from 'react'

export const ShowDelayMsContext = createContext<{ showDelayMs?: number }>({
    showDelayMs: undefined,
})

export const useSpinnerContextProps = (propsShowDelayMs?: number) => {
    const context = useContext(ShowDelayMsContext)

    return { showDelayMs: context.showDelayMs || propsShowDelayMs }
}
