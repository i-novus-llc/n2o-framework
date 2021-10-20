import React from "react";

function Card({ label, icon, href, className }) {
    return (
            <a href={href} className={className}>
                <img src={icon} alt={label} />
                <div>{label}</div>
            </a>
    )
}

export function Cards({ items = [], className}) {
    return (
            <div>
                {items.map(({ label, icon, href }) =>
                        <Card
                             key={label}
                             label={label}
                             icon={icon}
                             href={href}
                             className={className}
                        />)}
            </div>
    )
}
