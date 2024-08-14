import { ReactNode, SyntheticEvent } from 'react'

import { TBaseProps } from '../../types'

export enum Severity {
    danger = 'danger',
    info = 'info',
    success = 'success',
    warning = 'warning',
}

export enum AnimationDirection {
    default = 'default',
    reversed = 'reversed',
}

export type AlertProps = TBaseProps & {
    // Флаг показа кнопки закрытия
    animate?: boolean;
    // Подробности, находятся под текстом, показываются (скрываются) по клику на выделенный текст
    closeButton?: boolean;
    href?: string;
    // Флаг включения всплытия с анимацией
    loader?: boolean;
    onDismiss?(): void;
    // Временная точка отправки уведомления на клиент
    placement?: string;
    // Текст алерта
    severity?: Severity;
    // Цвет алерта
    stacktrace?: string | string[] | null;
    // Callback на закрытие
    stopRemoving?(): void;
    t?(arg: string): string;
    // Заголовок алерта
    text?: string;
    // Переход -> href по клику на alert
    timestamp?: number;
    title?: string;
}

type DefaultAlertOwnProps = {
    animationDirection: AnimationDirection;
    isField: boolean;
    onClose?(): void;
    stacktraceVisible?: boolean;
    togglingStacktrace(): void;
}

export type DefaultAlertProps = DefaultAlertOwnProps & Omit<AlertProps, 'visible' | 'loader' | 'placement' | 'stopRemoving'>

type AlertSectionOwnProps = {
    isSimple?: boolean;
    onClick?(e: SyntheticEvent): void;
    style?: AlertProps['style'] | null;
    text?: AlertProps['text'] | null;
    textClassName?: string;
}

export type AlertSectionProps = AlertSectionOwnProps & Pick<DefaultAlertProps, 'timestamp' | 'closeButton' | 'stacktrace' | 'stacktraceVisible' | 't'>

type AlertWrapperOwnProps = {
    children: ReactNode,
    severity: string
}

export type AlertWrapperProps = AlertWrapperOwnProps & Pick<DefaultAlertProps, 'className' | 'animate' | 'stacktrace' | 'href' | 'style' | 'animationDirection'>
