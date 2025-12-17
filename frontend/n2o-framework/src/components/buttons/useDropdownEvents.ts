import { useRef, useState } from 'react'

import useClickOutside from '../../core/hooks/useClickOutside'

// @INFO управляет состоянием, скрывает hint в открытом состоянии
export const useDropdownEvents = ({ hint }: { hint?: string }) => {
    const [isOpen, setIsOpen] = useState<boolean>(false)
    const clickOutsideRef = useRef(null)

    const onClick = (open?: boolean) => {
        if (typeof open === 'boolean') {
            setIsOpen(open)

            return
        }

        setIsOpen(!isOpen)
    }

    const onClickOutside = () => onClick(false)

    useClickOutside(onClickOutside, [clickOutsideRef])

    return {
        isOpen,
        onClick,
        hint: isOpen ? undefined : hint,
        clickOutsideRef,
    }
}
