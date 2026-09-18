package com.creative.isitvegan.testing

object TestTags {
    object Home {
        const val TOP_BAR_TITLE = "top_bar_title"
        const val BTN_SCAN = "btn_scan"
        const val TEXT_MAIN = "text_main"
        const val TEXT_SUB_MAIN = "text_sub_main"
        const val RECENT_LIST = "recent_list"
        fun recentItem(barcode: String) = "recent_item_$barcode"
    }

    object Scan {
        const val BTN_CLOSE = "btn_scan_close"
        const val TEXT_INSTRUCTION = "text_scan_instruction"
    }

    object Loading {
        const val ICON_ECO = "loading_icon_eco"
        const val PROGRESS_BAR = "loading_progress_bar"
        const val TEXT_MESSAGE = "loading_text_message"
    }

    object Product {
        const val TITLE = "product_title"
        const val BTN_BACK = "product_btn_back"
        const val IMAGE = "product_image"
        const val TEXT_NAME = "product_text_name"
        const val TEXT_BRAND = "product_text_brand"
        const val BANNER_STATUS = "product_banner_status"
        const val TEXT_STATUS = "product_text_status"
        const val SECTION_INFO = "product_section_info"
        const val SECTION_INGREDIENTS = "product_section_ingredients"
        fun ingredientItem(name: String) = "ingredient_item_$name"
    }

    object Error {
        const val TEXT_TITLE = "error_text_title"
        const val TEXT_DESC = "error_text_desc"
        const val BTN_HOME = "error_btn_home"
    }
}
