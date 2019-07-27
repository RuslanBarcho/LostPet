package io.vinter.lostpet.utils.config


class StyleConfig(val type: StyleType?, val statusBarColor: Int, val navBarColor: Int) {
    enum class StyleType {LIGHT, DARK}

    private constructor(builder: Builder) : this(builder.type, builder.statusBarColor, builder.navBarColor)

    class Builder {
        var type: StyleType? = null
        var statusBarColor: Int = 0
        var navBarColor: Int = 0

        fun type(type: StyleType) = apply { this.type = type }
        fun statusBarColor(color: Int) = apply { this.statusBarColor = color }
        fun navBarColor(color: Int) = apply { this.navBarColor = color }

        fun build() = StyleConfig(this)
    }
}