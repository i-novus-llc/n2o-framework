import { ReactNode, SyntheticEvent } from 'react'

import { TBaseProps } from '../../types'

export enum Severity {
    info = 'info',
    danger = 'danger',
    warning = 'warning',
    success = 'success'
}

export enum AnimationDirection {
    default = 'default',
    reversed = 'reversed'
}

export type AlertProps = TBaseProps & {
    title?: string; // Заголовок алерта
    text?: string; // Текст алерта
    severity?: Severity; // Цвет алерта
    stacktrace?: string | string[] | null; // Подробности, находятся под текстом, показываются (скрываются) по клику на выделенный текст
    closeButton?: boolean; // Флаг показа кнопки закрытия
    animate?: boolean; // Флаг включения всплытия с анимацией
    loader?: boolean;
    href?: string; // Переход -> href по клику на alert
    timestamp?: number; // Временная точка отправки уведомления на клиент
    placement?: string;
    onDismiss?(): void; // Callback на закрытие
    stopRemoving?(): void;
    t?(arg: string): string;
}

type DefaultAlertOwnProps = {
    stacktraceVisible?: boolean;
    isField: boolean;
    animationDirection: AnimationDirection;
    togglingStacktrace(): void;
    onClose?(): void;
}

export type DefaultAlertProps = DefaultAlertOwnProps & Omit<AlertProps, 'visible' | 'loader' | 'placement' | 'stopRemoving'>

type AlertSectionOwnProps = {
    textClassName?: string;
    isSimple?: boolean;
    text?: AlertProps['text'] | null;
    style?: AlertProps['style'] | null;
    onClick?(e: SyntheticEvent): void;
}

export type AlertSectionProps = AlertSectionOwnProps & Pick<DefaultAlertProps, 'timestamp'|'closeButton'|'stacktrace'|'stacktraceVisible'|'t'>

type AlertWrapperOwnProps = {
    severity: string,
    children: ReactNode
}

export type AlertWrapperProps = AlertWrapperOwnProps& Pick<DefaultAlertProps, 'className' | 'animate' | 'stacktrace' | 'href' | 'style' | 'animationDirection'>
