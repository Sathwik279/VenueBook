import { Code, Copyright } from "lucide-react";

export default function Footer() {
  return (
    <footer className="bg-gradient-to-b from-gray-50 to-gray-100 border-t border-gray-300 py-6 mt-8 shadow-inner">
      <div className="mx-auto px-4 flex flex-col items-center space-y-2 text-gray-600">
        {/* Bottom: product tagline */}
        <div className="flex flex-col items-center space-y-2">
          <div className="flex items-center space-x-2 hover:text-gray-800 transition-colors duration-300 font-medium">
            <Code className="w-5 h-5 text-blue-600" />
            <span className="text-md">Built for modern venue booking</span>
          </div>

          {/* Top: Copyright */}
          <div className="flex items-center space-x-2 text-sm hover:text-gray-800 transition-colors duration-300">
            <Copyright className="w-4 h-4" />
            <span className="font-medium">
              2025 | EasyVenue | All rights reserved.
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
}
